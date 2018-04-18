package ru.makar.icss.lab3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.makar.icss.lab3.api.MedicinesApi;
import ru.makar.icss.lab3.model.Medicine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class MedicineController implements MedicinesApi {

    private Integer idCounter = 1;
    private List<Medicine> medicines = new ArrayList<>();

    @Override
    public ResponseEntity<List<Medicine>> medicinesGet() {
        return ResponseEntity.ok(medicines);
    }

    @Override
    public ResponseEntity<Void> medicinesIdDelete(@PathVariable("id") Integer id) {
        if (medicines.removeIf(medicine -> id.equals(medicine.getId()))) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Medicine> medicinesIdGet(@PathVariable("id") Integer id) {
        return medicines.stream()
                .filter(medicine -> id.equals(medicine.getId()))
                .map(ResponseEntity::ok)
                .findFirst()
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Medicine> medicinesIdPatch(@PathVariable("id") Integer id, @RequestBody Medicine body) {
        Optional<Medicine> medicineOptional = medicines.stream()
                .filter(medicine -> id.equals(medicine.getId()))
                .findFirst();
        if (!medicineOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Medicine medicine = medicineOptional.get();
        if (body.getName() != null) {
            medicine.setName(body.getName());
        }
        if (body.getPrice() != null) {
            medicine.setPrice(body.getPrice());
        }
        if (body.getType() != null) {
            medicine.setType(body.getType());
        }
        return ResponseEntity.ok(medicine);
    }

    @Override
    public ResponseEntity<Medicine> medicinesIdPut(@PathVariable("id") Integer id, @RequestBody Medicine body) {
        Medicine medicine = medicines.stream().filter(m -> id.equals(m.getId())).findFirst().orElse(null);
        boolean created = false;
        if (medicine == null) {
            medicine = new Medicine();
            medicine.setId(idCounter++);
            created = true;
        }
        medicine.setName(body.getName());
        medicine.setPrice(body.getPrice());
        medicine.setType(body.getType());
        return new ResponseEntity<>(medicine, created ? HttpStatus.CREATED : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Medicine> medicinesPost(@RequestBody Medicine body) {
        body.setId(idCounter++);
        medicines.add(body);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }
}
