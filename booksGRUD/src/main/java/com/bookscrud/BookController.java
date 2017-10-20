package com.bookscrud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@RequestMapping(value = "/books")
@Controller
public class BookController {

    private final Logger logger = LoggerFactory.getLogger(BookController.class);
    private BookDao bookDao;
    private MessageSource messageSource;
    private SearchCriteria searchCriteriaRef;
    private static final int PAGING = 10;

    @RequestMapping(method = RequestMethod.GET)
    public String displayAll(@RequestParam(value = "page", required = false) Integer page, SearchCriteria searchCriteria, Model model, Locale locale) {

        if (searchCriteria != null)
            searchCriteriaRef = searchCriteria;

        List<String> criteriaArray = new ArrayList<String>();
        criteriaArray.add(messageSource.getMessage("label_book_title", new Object[]{}, locale));
        criteriaArray.add(messageSource.getMessage("label_book_author", new Object[]{}, locale));
        criteriaArray.add(messageSource.getMessage("label_book_year", new Object[]{}, locale));
        criteriaArray.add(messageSource.getMessage("label_book_read_already", new Object[]{}, locale));
        criteriaArray.add(messageSource.getMessage("label_book_isbn", new Object[]{}, locale));

        searchCriteriaRef.setCriteriaArray(criteriaArray);

        BookDao.Paginator paginator = new BookDao.Paginator(PAGING, bookDao.findAll());

        if (page == null || page < 1)
            page = 1;

        model.addAttribute("page", page);
        model.addAttribute("pageCount", paginator.getPageCount());
        model.addAttribute("books", paginator.getNextEntryListForPage(page));
        model.addAttribute("searchCriteria", searchCriteriaRef);

        return "books/list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String displayByCriteria(@RequestParam(value = "page", required = false) Integer page, SearchCriteria searchCriteria, Model model, Locale locale) {


        if (searchCriteria != null)
            searchCriteriaRef = searchCriteria;


        List<String> criteriaArray = new ArrayList<String>();
        criteriaArray.add(messageSource.getMessage("label_book_title", new Object[]{}, locale));
        criteriaArray.add(messageSource.getMessage("label_book_author", new Object[]{}, locale));
        criteriaArray.add(messageSource.getMessage("label_book_year", new Object[]{}, locale));
        criteriaArray.add(messageSource.getMessage("label_book_read_already", new Object[]{}, locale));
        criteriaArray.add(messageSource.getMessage("label_book_isbn", new Object[]{}, locale));

        searchCriteriaRef.setCriteriaArray(criteriaArray);


        List<Book> bookList = null;


        if (searchCriteriaRef.getType().equals(messageSource.getMessage(
                "label_book_title", new Object[]{}, locale))) {

            bookList = bookDao.findByTitle(searchCriteriaRef.getAttribute());

        }
        if (searchCriteriaRef.getType().equals(messageSource.getMessage(
                "label_book_author", new Object[]{}, locale))) {
            bookList = bookDao.findByAuthor(searchCriteriaRef.getAttribute());
        }
        if (searchCriteriaRef.getType().equals(messageSource.getMessage(
                "label_book_year", new Object[]{}, locale))) {
            bookList = bookDao.findByYear(Integer.valueOf(searchCriteriaRef.getAttribute()));
        }
        if (searchCriteriaRef.getType().equals(messageSource.getMessage(
                "label_book_read_already", new Object[]{}, locale))) {
            bookList = bookDao.searchByReadAlready(
                    Boolean.valueOf(searchCriteriaRef.getAttribute()));
        }
        if (searchCriteriaRef.getType().equals(messageSource.getMessage(
                "label_book_isbn", new Object[]{}, locale))) {
            bookList = bookDao.findByISBN(
                    searchCriteriaRef.getAttribute());
        }


        if (bookList == null || bookList.size() == 0) {
            model.addAttribute("message", "No results with type \"" + searchCriteriaRef.getType()
                    + "\" and value \"" +
                    (searchCriteriaRef.getAttribute() == "" ? "null" : searchCriteriaRef.getAttribute()) + "\"");
            bookList = bookDao.findAll();
        } else {
            model.addAttribute("message", "Find results: " + bookList.size());
        }

        BookDao.Paginator paginator = new BookDao.Paginator(PAGING, bookList);


        if (page == null || page < 1)
            page = 1;

        model.addAttribute("page", page);
        model.addAttribute("pageCount", paginator.getPageCount());
        model.addAttribute("books", paginator.getNextEntryListForPage(page));
        model.addAttribute("searchCriteria", searchCriteriaRef);

        return "books/list";
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam("id") Long id, Model model) {
        Book book = bookDao.findById(id);
        model.addAttribute("book", book);

        logger.info("Show book with id: " + book.getId());

        return "books/show";
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public String remove(@RequestParam("id") Long id) {

        Book book = bookDao.findById(id);
        bookDao.delete(book);

        logger.info("Book was read with id: " + id);

        return "redirect:/books";
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public String read(@RequestParam("id") Long id) {
        Book book = bookDao.findById(id);
        bookDao.read(book);

        logger.info("Book was read with id: " + book.getId());

        return "redirect:/books";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String toEditForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("book", bookDao.findById(id));
        return "books/edit";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String update(
            @Valid Book book, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Updating book");

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                logger.error(error.getObjectName() + ": " + error.getDefaultMessage());
            }

            model.addAttribute("message", new Message("error",
                    messageSource.getMessage("book_save_fail", new Object[]{}, locale)));

            model.addAttribute("book", book);

            return "books/edit";
        }
        model.asMap().clear();

        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("book_save_success", new Object[]{}, locale)));

        bookDao.changeEdition(book);

        logger.info("Book updated with id: " + book.getId());

        return "redirect:/books";
    }


    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String toCreateForm(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "books/create";
    }

    @RequestMapping(params = "form", method = RequestMethod.POST)
    public String create(
            @Valid Book book, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Adding book");

        if (bindingResult.hasErrors()) {

            for (FieldError error : bindingResult.getFieldErrors()) {
                logger.error(error.getObjectName() + ": " + error.getDefaultMessage());
            }

            model.addAttribute("message", new Message("error",
                    messageSource.getMessage("book_save_fail", new Object[]{}, locale)));

            model.addAttribute("book", book);

            return "books/create";
        }
        model.asMap().clear();

        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("book_save_success", new Object[]{}, locale)));

        bookDao.create(book);

        logger.info("Book Added with id: " + book.getId());

        return "redirect:/books";
    }

    @Autowired
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
