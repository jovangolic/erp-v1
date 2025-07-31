package com.jovan.erp_v1.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
// @NoArgsConstructor
@AllArgsConstructor
public class Shelf {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "row_count")
	private Integer rowCount;

	@Column
	private Integer cols;

	@ManyToOne
	@JoinColumn(name = "storage_id")
	private Storage storage;

	@OneToMany(mappedBy = "shelf", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Goods> goods;

	public Shelf() {
		this.goods = new ArrayList<>();
	}

	public void generateShelves(Storage storage, int rows, int columns) {
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= columns; j++) {
				Shelf shelf = new Shelf();
				shelf.setRowCount(i);
				shelf.setCols(j);
				shelf.setStorage(storage);
				storage.addShelf(shelf); // koristi helper metodu
			}
		}
	}
}
