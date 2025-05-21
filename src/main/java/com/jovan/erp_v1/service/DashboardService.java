package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jovan.erp_v1.dto.DailySalesDTO;
import com.jovan.erp_v1.enumeration.OrderStatus;
import com.jovan.erp_v1.repository.InventoryRepository;
import com.jovan.erp_v1.repository.ProcurementRepository;
import com.jovan.erp_v1.repository.ProductRepository;
import com.jovan.erp_v1.repository.SalesOrderRepository;
import com.jovan.erp_v1.repository.SalesRepository;
import com.jovan.erp_v1.repository.ShiftRepository;
import com.jovan.erp_v1.repository.UserRepository;
import com.jovan.erp_v1.repository.VendorRepository;
import com.jovan.erp_v1.response.DashboardResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final SalesRepository salesRepository;
    private final ProcurementRepository procurementRepository;
    private final ShiftRepository shiftRepository;

    @Override
    public DashboardResponse getDashboardData() {
        Long totalUsers = userRepository.count();
        Long totalInventories = inventoryRepository.count();
        Long totalSalesOrders = salesOrderRepository.count();
        Long totalVendors = vendorRepository.count();
        Long totalProducts = productRepository.count();
        Integer activeShifts = shiftRepository.countActiveShifts(); // custom query
        Integer pendingOrders = salesOrderRepository.countByStatus(OrderStatus.PENDING); // custom query
        BigDecimal totalRevenue = salesRepository.sumAllSalesRevenue(); // custom query
        BigDecimal totalProcurementCost = procurementRepository.sumTotalCost(); // custom query
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(6);
        List<DailySalesDTO> dailySalesList = salesRepository.getDailySalesLast7Days(sevenDaysAgo);
        // Ako ti treba kao mapa:
        Map<LocalDate, BigDecimal> dailySalesLast7Days = dailySalesList.stream()
                .collect(Collectors.toMap(DailySalesDTO::getDate, DailySalesDTO::getTotalSales));
        return new DashboardResponse(
                totalUsers,
                totalInventories,
                totalSalesOrders,
                totalVendors,
                totalProducts,
                totalRevenue,
                totalProcurementCost,
                activeShifts,
                pendingOrders,
                dailySalesLast7Days);
    }
}
