package com.yellowhouse.startuppostgressdocker.controller;

import com.yellowhouse.startuppostgressdocker.model.Capsules;
import com.yellowhouse.startuppostgressdocker.model.Clothes;
import com.yellowhouse.startuppostgressdocker.repository.CapsulesRepository;
import com.yellowhouse.startuppostgressdocker.repository.ClothesRepository;
import com.yellowhouse.startuppostgressdocker.service.CapsulesService;
import com.yellowhouse.startuppostgressdocker.service.ClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/capsules")
public class CapsulesController {
    @Autowired
    public CapsulesRepository capsulesRepository;

    @Autowired
    public ClothesRepository clothesRepository;
    @Autowired
    public CapsulesService capsulesService;

    @Autowired
    public ClothesService clothesService;

    @PostMapping()
    public ResponseEntity<Capsules> addCapsules(@RequestBody Capsules capsules){
         capsulesService.createCapsule(capsules);
        return ResponseEntity.ok().body(capsules);
    }

    @GetMapping()
    public ResponseEntity<List<Capsules>> findAll(){
        return ResponseEntity.ok(capsulesService.readAllCapsules());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Capsules> findCapsulesById(@PathVariable(value = "id") UUID capsulesId) {
        Capsules capsules = capsulesService.readCapsulesById(capsulesId);
        return ResponseEntity.ok().body(capsules);
    }


    @DeleteMapping()
    public ResponseEntity<Void> deleteCapsules(@PathVariable(value = "id") UUID capsulesId) {
        if (capsulesService.deleteClothesById(capsulesId) == true) {
            return ResponseEntity.ok().build();
        } else{
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/clothes")
    public Capsules putClothesInCapsula(@RequestParam (value ="capsuleId") UUID capsuleId
            ,@RequestParam (value = "clothesId") UUID clothesId){
        Capsules capsules = capsulesRepository.findById(capsuleId).get();
        Clothes clothes = clothesRepository.findById(clothesId).get();
        if (clothes.isInCapsula() == false) {
            clothes.setInCapsula(true);
            clothesRepository.save(clothes);
        }
        capsules.addClothesToCapsule(clothes);
        return capsulesRepository.save(capsules);
    }

}
