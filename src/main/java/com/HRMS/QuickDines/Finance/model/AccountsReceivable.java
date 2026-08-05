package com.HRMS.QuickDines.Finance.model;

import com.HRMS.QuickDines.CRM.model.Customer;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "accounts_receivable")
public class AccountsReceivable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private InvoiceManagement invoice;

    @Column(length = 50)
    private String invoiceNumber;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    @Column(precision = 12, scale = 2)
    private BigDecimal invoiceAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal receivedAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal balanceAmount;

    private String paymentStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}