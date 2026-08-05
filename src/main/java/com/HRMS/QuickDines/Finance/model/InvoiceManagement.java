package com.HRMS.QuickDines.Finance.model;

import com.HRMS.QuickDines.CRM.model.Customer;
import com.HRMS.QuickDines.Administration.model.Vendor;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "invoice_management", uniqueConstraints = {
                @UniqueConstraint(name = "uk_invoice_number", columnNames = "invoice_number")})
public class InvoiceManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "invoice_number",
            length = 50,
            nullable = false,
            unique = true
    )
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private String invoiceType;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    @Column(precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal discount;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;

    private String paymentStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}