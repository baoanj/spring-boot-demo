package xyz.baoanj.provider.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.baoanj.api.domain.Book;

@Mapper
public interface BookMapper {
    int addBook(Book book);
}
