package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VendorContractRepository {
    private final String vendorsFilePath;
    private final String contractsFilePath;
    private final List<Vendor> vendors;
    private final List<VendorContract> contracts;

    public VendorContractRepository(String vendorsFilePath, String contractsFilePath) {
        this.vendorsFilePath = vendorsFilePath;
        this.contractsFilePath = contractsFilePath;
        this.vendors = new ArrayList<>();
        this.contracts = new ArrayList<>();
        loadFromFiles();
    }

    public List<Vendor> getAllVendors() {
        return new ArrayList<>(vendors);
    }

    public List<VendorContract> getAllContracts() {
        return new ArrayList<>(contracts);
    }

    public Vendor findVendorById(int id) {
        return vendors.stream()
                .filter(v -> v.getVendorID() == id)
                .findFirst()
                .orElse(null);
    }

    public VendorContract findContractById(int id) {
        return contracts.stream()
                .filter(c -> c.getContractID() == id)
                .findFirst()
                .orElse(null);
    }

    public void addVendor(Vendor vendor) {
        vendors.add(vendor);
        saveVendors();
    }

    public void updateVendor(Vendor vendor) {
        Vendor existing = findVendorById(vendor.getVendorID());
        if (existing != null) {
            vendors.remove(existing);
            vendors.add(vendor);
            saveVendors();
        }
    }

    public void addContract(VendorContract contract) {
        contracts.add(contract);
        saveContracts();
    }

    public void updateContract(VendorContract contract) {
        VendorContract existing = findContractById(contract.getContractID());
        if (existing != null) {
            contracts.remove(existing);
            contracts.add(contract);
            saveContracts();
        }
    }

    private void loadFromFiles() {
        loadVendors();
        loadContracts();
    }

    private void loadVendors() {
        File file = new File(vendorsFilePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Vendor vendor = Vendor.fromCSV(line);
                if (vendor != null) {
                    vendors.add(vendor);
                }
            }
            System.out.println("Loaded " + vendors.size() + " vendors from file.");
        } catch (IOException e) {
            System.err.println("Error loading vendors: " + e.getMessage());
        }
    }

    private void loadContracts() {
        File file = new File(contractsFilePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                VendorContract contract = VendorContract.fromCSV(line, vendors);
                if (contract != null) {
                    contracts.add(contract);
                }
            }
            System.out.println("Loaded " + contracts.size() + " contracts from file.");
        } catch (IOException e) {
            System.err.println("Error loading contracts: " + e.getMessage());
        }
    }

    private void saveVendors() {
        ensureParentDirectory(vendorsFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(vendorsFilePath))) {
            for (Vendor vendor : vendors) {
                writer.write(vendor.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving vendors: " + e.getMessage());
        }
    }

    private void saveContracts() {
        ensureParentDirectory(contractsFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(contractsFilePath))) {
            for (VendorContract contract : contracts) {
                writer.write(contract.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving contracts: " + e.getMessage());
        }
    }

    private void ensureParentDirectory(String filename) {
        File f = new File(filename);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
}
