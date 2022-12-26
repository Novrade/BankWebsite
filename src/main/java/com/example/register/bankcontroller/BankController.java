package com.example.register.bankcontroller;

import com.example.register.bankrespository.BankRepository;
import com.example.register.userentity.Customer;
import com.example.register.userentity.CustomerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class BankController {

    @Autowired
    private BankRepository bankRepository;


    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer",customer);
        return "login";
    }

    @GetMapping ("/registration_form")
    public String showForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer",customer);
        return "registration_form";
    }

    @PostMapping("/register")
    public String saveUser(Customer customer) {
        System.out.println(customer);
        String encrypted = bCryptPasswordEncoder.encode(customer.getPassword());
        customer.setPassword(encrypted);
        bankRepository.save(customer);
        return "/login";
    }


    @GetMapping("/homepage")
    public String welcome( Model model, RedirectAttributes attributes, Principal principal) {
        String login = principal.getName();
        Customer customer = bankRepository.findByLogin(login);
        model.addAttribute("firstname", customer.getFirstName());
        model.addAttribute("lastname", customer.getLastName());
        return "homepage";
    }

    @GetMapping("/details")
    public String printDetails(Model model, Principal principal) {
        String login = principal.getName();
        Customer customer = bankRepository.findByLogin(login);
        model.addAttribute("customers",customer);
        return "details";
    }

    @GetMapping("/deposit")
    public String showDeposit(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer",customer);
        return "deposit";
    }
    @PostMapping("/depositsave")
    public String deposit(@ModelAttribute("password") String password ,@RequestParam("amount") double amount, Principal principal, RedirectAttributes redirectAttributes ) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String login = principal.getName();
        Customer customer = bankRepository.findByLogin(login);
        if(passwordEncoder.matches(password,customer.getPassword())) {
            customer.setBalance(customer.getBalance()+amount);
            bankRepository.save(customer);
            redirectAttributes.addFlashAttribute("message", "Success");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            customer.setAmount(0);
            return "redirect:/deposit";
        }else {
            redirectAttributes.addFlashAttribute("message", "Incorrect Password");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/deposit";
        }
    }

    @GetMapping("/withdraw")
    public String withdrawShow(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer",customer);
        return "withdraw";
    }

    @PostMapping("/withdrawsave")
    public String withdraw(@ModelAttribute("password") String password ,@RequestParam("amount") double amount, Principal principal, RedirectAttributes redirectAttributes) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String login = principal.getName();
        Customer customer = bankRepository.findByLogin(login);
        if(passwordEncoder.matches(password,customer.getPassword())) {
            if(customer.getBalance()-amount < 0) {
                redirectAttributes.addFlashAttribute("message", "Not enough funds");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
                return "redirect:/withdraw";
            }
            customer.setBalance(customer.getBalance()-amount);
            bankRepository.save(customer);
            redirectAttributes.addFlashAttribute("message", "Success");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            customer.setAmount(0);
            return "redirect:/withdraw";
        }else {
            redirectAttributes.addFlashAttribute("message", "Incorrect Password");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/withdraw";
        }
    }

    public Customer findCustomer(String password,Principal principal, BankRepository bankRepository) {
        String login = principal.getName();
        Customer customer = bankRepository.findByLogin(login);
        return customer;
    }
}
