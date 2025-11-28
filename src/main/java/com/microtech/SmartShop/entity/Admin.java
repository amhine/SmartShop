package com.microtech.SmartShop.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="admins")
@EqualsAndHashCode(callSuper=true)
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User{
}
