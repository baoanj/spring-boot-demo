package xyz.baoanj.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.baoanj.api.domain.Book;
import xyz.baoanj.api.service.BookDubboService;
import xyz.baoanj.provider.mapper.BookMapper;

@Service
public class BookDubboServiceImpl implements BookDubboService {
    @Autowired
    private BookMapper bookMapper;

    @Override
    public void addBook(Book book) {
        bookMapper.addBook(book);
        System.out.println("Dubbo provider addBook success!");
    }
}
