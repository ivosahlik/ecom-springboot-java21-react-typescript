package cz.ivosahlik.library.controller;

import cz.ivosahlik.library.annotation.UserType;
import cz.ivosahlik.library.requestmodels.AddBookRequest;
import cz.ivosahlik.library.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/secure/increase/book/quantity")
    public void increaseBookQuantity(@UserType String userType,
                                     @RequestParam Long bookId) throws Exception {
        if (userType == null || !userType.equals("admin")) {
            throw new Exception("Administration page only");
        }
        adminService.increaseBookQuantity(bookId);
    }

    @PutMapping("/secure/decrease/book/quantity")
    public void decreaseBookQuantity(@UserType String userType,
                                     @RequestParam Long bookId) throws Exception {
        if (userType == null || !userType.equals("admin")) {
            throw new Exception("Administration page only");
        }
        adminService.decreaseBookQuantity(bookId);
    }

    @PostMapping("/secure/add/book")
    public void postBook(@UserType String userType,
                         @RequestBody AddBookRequest addBookRequest) throws Exception {
        if (userType == null || !userType.equals("admin")) {
            log.error("Token validation failed for admin. UserType: {}", userType);
            throw new Exception("Administration page only");
        }
        try {
            adminService.postBook(addBookRequest);
        } catch (Exception e) {
            log.error("Error posting book: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/secure/delete/book")
    public void deleteBook(@UserType String userType,
                           @RequestParam Long bookId) throws Exception {
        if (userType == null || !userType.equals("admin")) {
            throw new Exception("Administration page only");
        }
        adminService.deleteBook(bookId);
    }

}












