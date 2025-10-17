package cz.ivosahlik.library.responsemodels;

import cz.ivosahlik.library.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ShelfCurrentLoansResponse {

    private Book book;
    private int daysLeft;
}
