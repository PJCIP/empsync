package com.srinidhi.lgb.Model;

public class Users {
    private String name, email1,phone, password,jobrole,department,vehiclename,VehicleModalName,VehicleCC;
    public Users()
    {

    }



    public Users(String name, String email1, String password, String phone, String jobrole,String department, String vehiclename, String VehicleModalName, String VehicleCC) {
        this.name = name;
        this.email1 = email1;
        this.phone=phone;
        this.password = password;
        this.jobrole=jobrole;
        this.department=department;
        this.vehiclename=vehiclename;
        this.VehicleModalName=VehicleModalName;
        this.VehicleCC=VehicleCC;


    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email1;
    }

    public void setEmail(String email1) {
        this.email1 = email1;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJobrole() {
        return jobrole;
    }

    public void setJobrole(String jobrole) {
        this.jobrole = jobrole;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getVehiclename() {
        return vehiclename;
    }

    public void setVehiclename(String vehiclename) {
        this.vehiclename = vehiclename;
    }

    public String getVehicleModalName() {
        return VehicleModalName;
    }

    public void setVehicleModalName(String VehicleModalName) {
        this.VehicleModalName = VehicleModalName;
    }

    public String getVehiclecc() {
        return VehicleCC;
    }

    public void setVehiclecc(String VehicleCC) {
        this.VehicleCC = VehicleCC;
    }

}
