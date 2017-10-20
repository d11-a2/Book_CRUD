package com.bookscrud;

import java.util.List;

public interface BookDao {

    Book create(Book book);

    Book read(Book book);

    Book changeEdition(Book book);

    void delete(Book book);

    List<Book> findAll();

    public List<Book> findByTitle(String title);

    public List<Book> findByYear(Integer year);

    public List<Book> findByAuthor(String author);

    public Book findById(long id);

    public List<Book> findByISBN(String ISBN);

    public List<Book> searchByReadAlready(Boolean readAlready);


    class Paginator {

        private int paging;
        private int pageCount;
        private int lastPageEntryCount;
        private List<Book> bookList;

        public Paginator(int paging, List<Book> bookList) {
            this.paging = paging;
            this.bookList = bookList;

            lastPageEntryCount = paging;

            pageCount = bookList.size() / paging;

            if (bookList.size() % paging > 0) {
                pageCount++;
                lastPageEntryCount = bookList.size() % paging;
            }
        }

        public List<Book> getNextEntryListForPage(int nextPage) {

            if (nextPage < 1)
                nextPage = 1;

            if (nextPage > pageCount)
                nextPage = pageCount;

            int beginIndex = (nextPage - 1) * paging;

            if (nextPage == pageCount)
                return bookList.subList(beginIndex, beginIndex + lastPageEntryCount);

            return bookList.subList(beginIndex, beginIndex + paging);
        }

        public int getPageCount() {
            return pageCount;
        }
    }

}
