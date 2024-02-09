package com.halcyon.udemy.service;

import com.halcyon.udemy.model.ContentOrder;
import com.halcyon.udemy.model.Image;
import com.halcyon.udemy.model.Page;
import com.halcyon.udemy.repository.ImageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final PageService pageService;
    private final ContentOrderService contentOrderService;

    private final HttpServletRequest request;

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png");

    public Image create(MultipartFile file, Long pageId) {
        Page page = pageService.getPage(pageId);
        Integer order = contentOrderService.getLastOrderForPage(page.getId()) + 1;

        String filePath = transferImage(file);

        Image image = imageRepository.save(new Image(filePath, page));
        ContentOrder contentOrder = contentOrderService.save(
                new ContentOrder(page.getId(), image.getId(), "image", order)
        );

        image.setContentOrderId(contentOrder.getId());
        return imageRepository.save(image);
    }

    public Image create(MultipartFile file, Long pageId, Integer order) {
        Page page = pageService.getPage(pageId);
        Integer lastOrder = contentOrderService.getLastOrderForPage(page.getId()) + 1;

        if (order > lastOrder) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The image order exceeds the last one created.");
        }

        String filePath = transferImage(file);
        Image image = imageRepository.save(new Image(filePath, page));

        List<ContentOrder> contentOrders = contentOrderService.findAllByPageIdAndOrder(page.getId(), order - 1);
        for (ContentOrder contentOrder: contentOrders) {
            contentOrder.setOrder(contentOrder.getOrder() + 1);
        }

        contentOrderService.saveAll(contentOrders);

        ContentOrder contentOrder = contentOrderService.save(
                new ContentOrder(page.getId(), image.getId(), "image", order)
        );

        image.setContentOrderId(contentOrder.getId());
        return imageRepository.save(image);
    }

    public ContentOrder changeOrderById(Long imageId, Integer order) {
        Image image = findById(imageId);
        ContentOrder imageContentOrder = contentOrderService.findById(image.getContentOrderId());
        int imageOrder = imageContentOrder.getOrder();

        int minOrder = Math.min(imageOrder, order);
        int maxOrder = Math.max(imageOrder, order);

        List<ContentOrder> contentOrders = contentOrderService.findAllByPageIdInInterval(
                image.getPage().getId(),
                minOrder,
                maxOrder
        );

        for (ContentOrder contentOrder: contentOrders) {
            if (contentOrder == imageContentOrder) {
                continue;
            }

            if (order == minOrder) {
                contentOrder.setOrder(contentOrder.getOrder() + 1);
            } else {
                contentOrder.setOrder(contentOrder.getOrder() - 1);
            }
        }

        contentOrderService.saveAll(contentOrders);

        imageContentOrder.setOrder(order);
        return contentOrderService.save(imageContentOrder);
    }

    public String deleteById(Long imageId) {
        Image image = findById(imageId);
        imageRepository.delete(image);

        Long pageId = image.getPage().getId();
        Integer order = contentOrderService.findById(image.getContentOrderId()).getOrder();
        contentOrderService.deleteById(image.getContentOrderId());

        List<ContentOrder> contentOrders = contentOrderService.findAllByPageIdAndOrder(pageId, order);
        for (ContentOrder contentOrder: contentOrders) {
            contentOrder.setOrder(contentOrder.getOrder() - 1);
        }

        contentOrderService.saveAll(contentOrders);
        return "Image deleted successfully.";
    }

    public Image findById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image with this id not found."));
    }

    public List<Image> findAllByPageId(Long pageId) {
        return imageRepository.findAllByPageId(pageId);
    }

    private String getFilePath(MultipartFile file) {
        String filePath = request.getServletContext().getRealPath("/uploads/");
        if (! new File(filePath).exists()) {
            new File(filePath).mkdir();
        }

        String originalFileName = Objects.requireNonNull(file.getOriginalFilename())
                .replace("/", "")
                .replace("\\", "")
                .replace("\\", "")
                .replace(":", "")
                .replace("?", "")
                .replace("*", "")
                .replace("\"", "")
                .replace("|", "")
                .replace("<", "")
                .replace(">", "");

        String fileName = new Random().nextInt(10000000) + originalFileName;

        return String.format("%s/%s", filePath, fileName);
    }

    private String transferImage(MultipartFile file) {
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image type should be jpeg/png.");
        }

        String filePath = getFilePath(file);

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return filePath;
    }
}
