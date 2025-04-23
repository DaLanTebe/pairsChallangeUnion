package sia.productevent.event;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductEvent implements Serializable{

        private int id;

        private String name;

        private String description;

        private BigDecimal price;

        private Integer quantity;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        private String message;

        public ProductEvent(String message, int id, String name, String description, BigDecimal price, Integer quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
                this.id = id;
                this.name = name;
                this.description = description;
                this.price = price;
                this.quantity = quantity;
                this.createdAt = createdAt;
                this.updatedAt = updatedAt;
                this.message = message + " Message Time: " + LocalDateTime.now();
        }

        public ProductEvent() {}

        public int getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getDescription() {
                return description;
        }

        public BigDecimal getPrice() {
                return price;
        }

        public Integer getQuantity() {
                return quantity;
        }

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
                return updatedAt;
        }

        public String getMessage() {
                return message;
        }

        public void setId(int id) {
                this.id = id;
        }

        public void setName(String name) {
                this.name = name;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public void setPrice(BigDecimal price) {
                this.price = price;
        }

        public void setQuantity(Integer quantity) {
                this.quantity = quantity;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
                this.createdAt = createdAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
                this.updatedAt = updatedAt;
        }

        public void setMessage(String message) {
                this.message = message;
        }

        @Override
        public String toString() {
                return "ProductEvent{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", description='" + description + '\'' +
                        ", price=" + price +
                        ", quantity=" + quantity +
                        ", createdAt=" + createdAt +
                        ", updatedAt=" + updatedAt +
                        ", message='" + message + '\'' +
                        '}';
        }
}
