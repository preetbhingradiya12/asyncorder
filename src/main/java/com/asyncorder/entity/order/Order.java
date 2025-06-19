    package com.asyncorder.entity.order;

    import com.asyncorder.entity.User;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;
    import org.hibernate.annotations.UuidGenerator;

    import java.time.Instant;
    import java.util.UUID;

    @Entity
    @Table(name = "orders")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class Order {

        @Id
        @UuidGenerator
        private UUID id;

        @Column(name = "productName", nullable = false)
        private  String productName;

        @Column(name = "quantity", nullable = false)
        private Integer quantity;

        @Column(nullable = false)
        private String item;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private OrderStatus status;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId", nullable = false)
        private User user;

        @CreationTimestamp
        private Instant createdAt;

        @UpdateTimestamp
        private Instant updatedAt;
    }
