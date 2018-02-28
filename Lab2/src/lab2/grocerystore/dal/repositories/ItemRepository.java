package lab2.grocerystore.dal.repositories;

import java.io.File;
import java.util.List;

import lab2.grocerystore.models.Item;

public class ItemRepository extends JsonRepository<Item> {
	public final String imageFolder = "src/lab2/images/";
	
	public ItemRepository() {
		super("src/itemDatabase.json", Item.class);

	}
	
	public File getImageFile(int itemId) {
		return new File(imageFolder + itemId + ".png");
	}
	
	public void update(int itemId, Item updated) {
		List<Item> allItems = getAll();
		
		allItems.stream()
			.filter(x -> x.getId() == itemId)
			.forEach(x -> {
				//x.setName(updated.getName());
				//x.setPricePerUnit(updated.getPricePerUnit());
				x.setQuantity(updated.getQuantity());
			});
		
		this.dumpToFile(allItems);
	}

}
