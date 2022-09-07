package com.example.Test.controllers;

import com.example.Test.Repositoriy.BananasRepository;
import com.example.Test.Repositoriy.NuwesRepositoriy;
import com.example.Test.models.Bananas;
import com.example.Test.models.Monke;
import com.example.Test.models.Nuwes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/Bananas")
public class BananaController {

    @Autowired
    private BananasRepository bananasRepository;

    @GetMapping("/")
    public  String Index(Model model){
        Iterable<Bananas> bananas =  bananasRepository.findAll();
        model.addAttribute("bananas", bananas);
        return "Bananas/index";
    }

    @GetMapping("/addView")
    public  String addView(Model model){

        return "Bananas/add";
    }

    @PostMapping("/add")
    public  String add(
            @RequestParam("name") String name,
            @RequestParam("opisanie") String opisanie,
            @RequestParam("companiyaPostavki") String companiyaPostavki,
            @RequestParam("Cost") int Cost,
            @RequestParam("colZakazov") int colZakazov,

            Model model){

        Bananas bananas = new Bananas(name,opisanie,companiyaPostavki,Cost,colZakazov);
        bananasRepository.save(bananas);
        return "redirect:/Bananas/";
    }

    @GetMapping("/search")
    public  String searchMetrhod(
            @RequestParam("name") String name,
            Model model)
    {
        if(name.isEmpty())
        {
            Iterable<Bananas> bananas =  bananasRepository.findAll();
            model.addAttribute("bananas", bananas);
            return "Bananas/index";
        }
        else {
            List<Bananas> bananasList = bananasRepository.findByName(name);
            model.addAttribute("bananas", bananasList);
            return "/Bananas/index";
        }
    }
    @GetMapping("/searchContaints")
    public  String searchMetrhodContains(
            @RequestParam("name") String name,
            Model model)
    {
        if(name.isEmpty())
        {
            Iterable<Bananas> bananas =  bananasRepository.findAll();
            model.addAttribute("bananas", bananas);
            return "Bananas/index";
        }
        else {
            List<Bananas> bananasList = bananasRepository.findByNameContains(name);
            model.addAttribute("bananas", bananasList);
            return "/Bananas/index";
        }
    }

    @GetMapping("/{id}")
    public  String read(
            @PathVariable("id")Long id,
            Model model)
    {
        Optional<Bananas> news = bananasRepository.findById(id);
        ArrayList<Bananas> newArrayList = new ArrayList<>();
        news.ifPresent(newArrayList::add);
        model.addAttribute("bananas",newArrayList);
        return "Bananas/Info";
    }

    @GetMapping("/del/{id}")
    public  String del(
            @PathVariable("id")Long id)
    {
        Bananas news = bananasRepository.findById(id).orElseThrow();
        bananasRepository.delete(news);
        return "redirect:/Bananas/";
    }


    @GetMapping("/edit/{id}")
    public  String edit(
            @PathVariable("id")Long id,
            Model model
    )
    {
        if(!bananasRepository.existsById(id))
        {
            return "redirect:/Bananas/";
        }
        Optional<Bananas> news = bananasRepository.findById(id);
        ArrayList<Bananas> newArrayList = new ArrayList<>();
        news.ifPresent(newArrayList::add);
        model.addAttribute("bananas",newArrayList);

        Bananas bananas = newArrayList.get(0);
        model.addAttribute("name",bananas.getName());
        model.addAttribute("opisanie",bananas.getOpisanie());
        model.addAttribute("companiyaPostavki",bananas.getCompaniyaPostavki());
        model.addAttribute("Cost",bananas.getCost());
        model.addAttribute("colZakazov",bananas.getColZakazov());

        return "Bananas/Edit";
    }

    @PostMapping("/edit/{id}")
    public  String editNews(
            @PathVariable("id")Long id,
            @RequestParam("name")String name,
            @RequestParam("opisanie")String opisanie,
            @RequestParam("companiyaPostavki")String companiyaPostavki,
            @RequestParam("Cost")String  Cost,
            @RequestParam("colZakazov")String colZakazov,
            Model model
    )
    {
        if(!bananasRepository.existsById(id))
        {
            return "redirect:/Bananas/";
        }

        if(name.isEmpty() || opisanie.isEmpty() || companiyaPostavki.isEmpty() || Cost.isEmpty() || colZakazov.isEmpty())
        {
            return "redirect:/Bananas/";
        }

        Bananas news = bananasRepository.findById(id).orElseThrow();

        news.setName(name);
        news.setOpisanie(opisanie);
        news.setCompaniyaPostavki(companiyaPostavki);

        news.setCost(Integer.parseInt(Cost));
        news.setColZakazov(Integer.parseInt(colZakazov));



        bananasRepository.save(news);
        return "redirect:/Bananas/";
    }
}
