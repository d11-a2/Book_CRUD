package com.bookscrud;

import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.List;

public class BookSample {

    public static void main(String[] args) {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.load("/META-INF/spring/app-context.xml");
        context.refresh();

        BookDao bookService = context.getBean("bookDao", BookDao.class);

        System.out.println("by id " + bookService.findById(2L));
        List<Book> books = bookService.findAll();

        for (Book book : books) {
            System.out.println(book.toString());
        }
        System.out.println("__________________________________________________________________");

        Book book = new Book();
        book.setId(26);
        book.setTitle("new book");
        book.setDescription("nothing");
        book.setAuthor("noname");
        book.setIsbn("1221");
        book.setPrintYear(1990);

        // bookService.update(book);

        // bookService.create(book);

        //bookService.delete(book);

        //bookService.read(book);

        //books = bookService.findAll();



    }
}
