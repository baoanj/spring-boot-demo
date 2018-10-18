package xyz.baoanj.contacts.service;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import xyz.baoanj.api.domain.Book;
import xyz.baoanj.api.service.BookDubboService;

import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {
    @Reference
    private BookDubboService bookDubboService;

    public void addBook(String name, int page) {
        Book book = new Book();
        String uuid = UUID.randomUUID().toString();
        book.setBookId(uuid);
        book.setBookName(name);
        book.setBookPage(page);
        bookDubboService.addBook(book);
    }
}
