package com.jovan.erp_v1.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Dashboard {

    // Ukupan broj korisnika u sistemu
    private long totalUsers;

    // Broj radnika po ulozi (ADMIN, EMPLOYEE, FOREMAN itd.)
    private Map<String, Long> userCountByRole;

    // Ukupan broj skladišta
    private long totalStorages;

    // Ukupan broj proizvoda (Goods)
    private long totalGoods;

    // Ukupan broj narudžbina
    private long totalSalesOrders;

    // Ukupan broj realizovanih prodaja
    private long totalSales;

    // Ukupan broj nabavki (Procurements)
    private long totalProcurements;

    // Ukupan broj inventura
    private long totalInventories;

    // Broj neusaglašenih inventura
    private long unalignedInventories;

    // Broj izveštaja smena
    private long totalShiftReports;

    // Broj smena danas/ove nedelje
    private long shiftsToday;
    private long shiftsThisWeek;

    // Ukupan prihod (suma iz Sales)
    private BigDecimal totalRevenue;

    // Ukupan trošak (suma iz Procurement/SupplyItem)
    private BigDecimal totalCost;

    // Broj neplaćenih faktura
    private long unpaidInvoices;

    // Vremenska serija prodaja po danima (za grafikon)
    private Map<LocalDate, BigDecimal> dailySales;

    // Vremenska serija troškova po danima
    private Map<LocalDate, BigDecimal> dailyCosts;

    // Lista poslednjih 5 narudžbina
    private List<SalesOrder> recentSalesOrders;

    // Lista poslednjih 5 smenskih izveštaja
    private List<ShiftReport> recentShiftReports;

    // Lista skladišta sa najnižom popunjenošću
    private List<StorageStatus> underutilizedStorages;

    // Pomoćna klasa za prikaz statusa skladišta
    @Data
    public static class StorageStatus {
        private String storageName;
        private double capacity;
        private double usedCapacity; // računato
        private double utilizationPercentage;
    }

}
