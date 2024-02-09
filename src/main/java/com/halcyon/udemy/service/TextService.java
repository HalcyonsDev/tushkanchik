package com.halcyon.udemy.service;

import com.halcyon.udemy.dto.NewTextDto;
import com.halcyon.udemy.model.ContentOrder;
import com.halcyon.udemy.model.Page;
import com.halcyon.udemy.model.Text;
import com.halcyon.udemy.repository.TextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TextService {
    private final TextRepository textRepository;
    private final PageService pageService;
    private final ContentOrderService contentOrderService;

    public Text create(NewTextDto dto) {
        Page page = pageService.getPage(dto.getPageId());
        Integer order = contentOrderService.getLastOrderForPage(page.getId()) + 1;

        Text text = textRepository.save(new Text(dto.getContent(), page));
        ContentOrder contentOrder = contentOrderService.save(
                new ContentOrder(page.getId(), text.getId(), "text", order)
        );

        text.setContentOrderId(contentOrder.getId());
        return textRepository.save(text);
    }

    public Text create(NewTextDto dto, Integer order) {
        Page page = pageService.getPage(dto.getPageId());
        Integer lastOrder = contentOrderService.getLastOrderForPage(page.getId()) + 1;

        if (order > lastOrder) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The text order exceeds the last one created.");
        }

        Text text = textRepository.save(new Text(dto.getContent(), page));

        List<ContentOrder> contentOrders = contentOrderService.findAllByPageIdAndOrder(page.getId(), order - 1);
        for (ContentOrder contentOrder: contentOrders) {
            contentOrder.setOrder(contentOrder.getOrder() + 1);
        }

        contentOrderService.saveAll(contentOrders);

        ContentOrder contentOrder = contentOrderService.save(
                new ContentOrder(page.getId(), text.getId(), "text", order)
        );

        text.setContentOrderId(contentOrder.getId());
        return textRepository.save(text);
    }

    public ContentOrder changeOrderById(Long textId, Integer order) {
        Text text = findById(textId);
        ContentOrder textContentOrder = contentOrderService.findById(text.getContentOrderId());
        int textOrder = textContentOrder.getOrder();

        int minOrder = Math.min(textOrder, order);
        int maxOrder = Math.max(textOrder, order);

        List<ContentOrder> contentOrders = contentOrderService.findAllByPageIdInInterval(
                text.getPage().getId(),
                minOrder,
                maxOrder
        );

        for (ContentOrder contentOrder: contentOrders) {
            if (contentOrder == textContentOrder) {
                continue;
            }

            if (order == minOrder) {
                contentOrder.setOrder(contentOrder.getOrder() + 1);
            } else {
                contentOrder.setOrder(contentOrder.getOrder() - 1);
            }
        }

        contentOrderService.saveAll(contentOrders);

        textContentOrder.setOrder(order);
        return contentOrderService.save(textContentOrder);
    }

    public String deleteById(Long textId) {
        Text text = findById(textId);
        textRepository.delete(text);

        Long pageId = text.getPage().getId();
        Integer order = contentOrderService.findById(text.getContentOrderId()).getOrder();
        contentOrderService.deleteById(text.getContentOrderId());

        List<ContentOrder> contentOrders = contentOrderService.findAllByPageIdAndOrder(pageId, order);
        for (ContentOrder contentOrder: contentOrders) {
            contentOrder.setOrder(contentOrder.getOrder() - 1);
        }

        contentOrderService.saveAll(contentOrders);
        return "Text deleted successfully.";
    }

    public Text findById(Long textId) {
        return textRepository.findById(textId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Text with this id not found."));
    }
}
