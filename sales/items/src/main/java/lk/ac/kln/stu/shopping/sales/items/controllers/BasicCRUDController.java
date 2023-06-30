package lk.ac.kln.stu.shopping.sales.items.controllers;

import lk.ac.kln.stu.shopping.sales.items.models.SalesItem;
import lk.ac.kln.stu.shopping.sales.items.services.SalesItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"sales/items", "sales/items/"})
public class BasicCRUDController {

    private final SalesItemsService salesItemsService;

    @Autowired
    public BasicCRUDController(SalesItemsService salesItemsService) {
        this.salesItemsService = salesItemsService;
    }

    @GetMapping
    public List<SalesItem> index() {
        return this.salesItemsService.getAllSalesItems();
    }

    @GetMapping("/id={id}")
    public ResponseEntity<SalesItem> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(this.salesItemsService.getSalesItemById(id));
        }

        catch (IllegalStateException illegalStateException) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @GetMapping("/name={name}")
    public ResponseEntity<SalesItem> getByName(@PathVariable String name) {
        try {
            return ResponseEntity.ok(this.salesItemsService.getSalesItemsByName(name));
        }

        catch (IllegalStateException illegalStateException) {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @PostMapping
    public void add(@RequestBody List<SalesItem> salesItems) {
        this.salesItemsService.createNewSalesItems(salesItems);
    }

    @PutMapping
    public void update(@RequestBody SalesItem salesItem) {
        this.salesItemsService.updateSalesItem(salesItem);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.salesItemsService.deleteSalesItemById(id);
    }

}
