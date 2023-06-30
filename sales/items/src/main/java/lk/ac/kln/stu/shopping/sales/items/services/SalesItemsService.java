package lk.ac.kln.stu.shopping.sales.items.services;

import lk.ac.kln.stu.shopping.sales.items.models.SalesItem;
import lk.ac.kln.stu.shopping.sales.items.repositories.SalesItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalesItemsService {

    private final SalesItemRepository salesItemRepository;

    @Autowired
    public SalesItemsService(SalesItemRepository salesItemRepository) {
        this.salesItemRepository = salesItemRepository;
    }

    public List<SalesItem> getAllSalesItems() {
        return this.salesItemRepository.findAll();
    }

    public SalesItem getSalesItemById(Long id) {
        Optional<SalesItem> salesItem = this.salesItemRepository.findById(id);
        if (salesItem.isPresent()) {
            return salesItem.get();
        }
        else {
            throw new IllegalStateException("No such sales item found");
        }
    }

    public SalesItem getSalesItemsByName(String name) {
        Optional<SalesItem> salesItem = this.salesItemRepository.findSalesItemByName(name);
        if (salesItem.isPresent()) {
            return salesItem.get();
        }
        else {
            throw new IllegalStateException("No such sales item found");
        }
    }

    public void createNewSalesItems(List<SalesItem> salesItems) {
        for (SalesItem salesItem : salesItems) {
            if (this.salesItemRepository.findSalesItemByName(salesItem.getName()).isPresent()) {
                throw new IllegalStateException("A sales item with the name: " + salesItem.getName() + " already exists.");
            }
        }

        this.salesItemRepository.saveAll(salesItems);
    }

    public void updateSalesItem(SalesItem salesItem) {
        if (this.salesItemRepository.existsById(salesItem.getId())) {
            this.salesItemRepository.save(salesItem);
        }
        else {
            throw new IllegalStateException("No such sales item found!");
        }
    }

    public void deleteSalesItemById(Long id) {
        if (this.salesItemRepository.existsById(id)) {
            this.salesItemRepository.deleteById(id);
        }
        else {
            throw new IllegalStateException("No such sales item found!");
        }
    }

}
