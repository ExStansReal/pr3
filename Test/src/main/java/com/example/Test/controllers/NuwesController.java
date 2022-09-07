package com.example.Test.controllers;

import com.example.Test.Repositoriy.NuwesRepositoriy;
import com.example.Test.models.Bananas;
import com.example.Test.models.Nuwes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/News")
public class NuwesController {

    @Autowired
    private NuwesRepositoriy newsRepository;

    @GetMapping("/")
    public  String Index(Model model){
        Iterable<Nuwes> news =  newsRepository.findAll();
        model.addAttribute("news", news);
        return "News/index";
    }

    @GetMapping("/addView")
    public  String addView(Model model){

        return "News/add";
    }

    @PostMapping("/add")
    public  String add(
            @RequestParam("title") String title,
            @RequestParam("body") String author,
            @RequestParam("text") String textNews,
            @RequestParam("vuews") int Views,
            @RequestParam("likes") int likes,

            Model model){

        Nuwes newOne = new Nuwes(title,textNews,author,Views,likes);
        newsRepository.save(newOne);
        return "redirect:/News/";
    }

    @GetMapping("/search")
    public  String searchMetrhod(
            @RequestParam("title") String title,
            Model model)
    {
        List<Nuwes> newList = newsRepository.findByTitle(title);
        model.addAttribute("news",newList);
        return "News/index";
    }

    @GetMapping("/searchContaints")
    public  String searchMetrhodContains(
            @RequestParam("name") String name,
            Model model)
    {
        if(name.isEmpty())
        {
            Iterable<Nuwes> bananas =  newsRepository.findAll();
            model.addAttribute("news", bananas);
            return "News/index";
        }
        else {
            List<Nuwes> bananasList = newsRepository.findByTitleContains(name);
            model.addAttribute("news", bananasList);
            return "News/index";
        }
    }

    @GetMapping("/{id}")
    public  String read(
            @PathVariable("id")Long id,
            Model model)
    {
        Optional<Nuwes> news = newsRepository.findById(id);
        ArrayList<Nuwes> newArrayList = new ArrayList<>();
        news.ifPresent(newArrayList::add);
        model.addAttribute("news",newArrayList);
        return "News/InfoNews";
    }

    @GetMapping("/del/{id}")
    public  String del(
            @PathVariable("id")Long id)
    {
        Nuwes news = newsRepository.findById(id).orElseThrow();
        newsRepository.delete(news);
        return "redirect:/News/";
    }


    @GetMapping("/edit/{id}")
    public  String edit(
            @PathVariable("id")Long id,
            Model model
    )
    {
        if(!newsRepository.existsById(id))
        {
            return "redirect:/News/";
        }
        Optional<Nuwes> news = newsRepository.findById(id);
        ArrayList<Nuwes> newArrayList = new ArrayList<>();
        news.ifPresent(newArrayList::add);
        model.addAttribute("news",newArrayList);

        return "News/EditNews";
    }

    @PostMapping("/edit/{id}")
    public  String editNews(
            @PathVariable("id")Long id,
            @RequestParam("title")String title,
    @RequestParam("body")String author,
    @RequestParam("text")String text,
    @RequestParam("vuews")Integer vuews,
            @RequestParam("likes")Integer likes,
            Model model
    )
    {
        if(!newsRepository.existsById(id))
        {
            return "redirect:/News/";
        }
        Nuwes news = newsRepository.findById(id).orElseThrow();

        news.setTitle(title);
        news.setBody(author);
        news.setText(text);
        news.setVuews(vuews);
        news.setLikes(likes);

        newsRepository.save(news);
        return "redirect:/News/";
    }

}
