package com.jovan.erp_v1.model;

import java.util.ArrayList;
import java.util.List;

import com.jovan.erp_v1.enumeration.GoodsType;
import com.jovan.erp_v1.enumeration.StorageType;
import com.jovan.erp_v1.enumeration.SupplierType;
import com.jovan.erp_v1.enumeration.UnitMeasure;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
// @DiscriminatorColumn(name = "goods_type", discriminatorType =
// DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BarCode> barCodes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitMeasure unitMeasure;

    @Enumerated(EnumType.STRING)
    @Column
    private SupplierType supplierType;

    @Enumerated(EnumType.STRING)
    @Column
    private StorageType storageType;

    /*
     * @Column(name = "goods_enum_type", nullable = false)
     * private GoodsType goodsType;
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoodsType goodsType;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private Supply supply;

    @ManyToOne
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    public void addBarCode(BarCode barCode) {
        if (this.barCodes == null) {
            this.barCodes = new ArrayList<>();
        }
        barCode.setGoods(this);
        this.barCodes.add(barCode);
    }
}
