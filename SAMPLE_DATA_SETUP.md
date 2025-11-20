# 📦 SAMPLE DATA SETUP

## Quick Start with Pre-loaded Data

If you want to start your demo with existing data (to show persistence and avoid repetitive data entry), use these sample files.

---

## Option 1: Start Fresh (Recommended for Full Demo)

**Delete existing data files:**
```bash
cd /home/rahum/coms362finalproject
rm -f data/vendors.txt data/contracts.txt data/specifications.txt data/prototypes.txt data/feedback.txt
```

**Then follow the DEMO_SCRIPT.md from Step 1**

---

## Option 2: Start with Sample Data

### Create Sample Vendors

**Create:** `data/vendors.txt`

```csv
1,EcoFabrics Inc,contact@ecofabrics.com,false,false,Business License;Tax Certificate;Sustainability Certification
2,Premium Textiles Co,sales@premiumtextiles.com,true,false,Business License;ISO 9001 Certificate;Quality Assurance Docs
3,Blocked Vendor LLC,info@blocked.com,false,true,Business License
```

**Explanation:**
- Vendor 1: Fresh vendor ready for contract approval demo
- Vendor 2: Already approved vendor (shows approved state)
- Vendor 3: Blocked vendor (shows blocking mechanism)

---

### Create Sample Contracts

**Create:** `data/contracts.txt`

```csv
1,1,Pending,Net 30 payment terms,100% organic materials,50000.00,,
2,2,Approved,Net 60 payment terms,50% recycled materials,75000.00,Budget verified and approved,All compliance requirements met. Approved for partnership.
3,1,Finance Review,Bulk discount available,Zero-waste packaging required,35000.00,Under review,,
```

**Explanation:**
- Contract 1: Pending, ready for finance review
- Contract 2: Fully approved (shows completed workflow)
- Contract 3: In finance review (shows mid-workflow state)

---

### Create Sample Material Specifications

**Create:** `data/specifications.txt`

```csv
1,Smooth,150.0,100% recycled materials,Navy Blue,Matte
2,Textured,200.0,Organic cotton,Natural Beige,Glossy
3,Soft,180.0,Bamboo fiber blend,Forest Green,Satin
```

**Explanation:**
- Multiple specs available for prototype creation
- Shows variety in material properties

---

### Create Sample Prototypes

**Create:** `data/prototypes.txt`

```csv
1,Eco-Denim X1,Testing,70% recycled cotton\, 28% organic cotton\, 2% elastane,1,1,1;2
2,Sustainable Silk Pro,Approved,100% organic silk,2,2,
3,Bamboo Blend V2,In Development,60% bamboo\, 38% organic cotton\, 2% spandex,3,3,
```

**Explanation:**
- Prototype 1: In testing with feedback (ready for demo)
- Prototype 2: Approved (shows completed state)
- Prototype 3: In development (early stage)

---

### Create Sample Feedback

**Create:** `data/feedback.txt`

```csv
1,Good fabric strength. Minimal fraying during cutting. Suitable for industrial production.,4,Manufacturing,Mike Rodriguez
2,Color consistency excellent. Texture feels premium. Stone-wash needs adjustment.,3,Design,Alice Williams
3,Excellent drape and sheen. Perfect for evening wear collection.,5,Design,Alice Williams
4,Easy to work with. No special equipment needed.,5,Manufacturing,Mike Rodriguez
```

**Explanation:**
- Feedback 1-2: Linked to Prototype 1 (shown in prototype CSV)
- Feedback 3-4: Available for other prototypes
- Shows variety of ratings and comments

---

## 🚀 Quick Setup Script

**Create all sample files at once:**

```bash
#!/bin/bash
cd /home/rahum/coms362finalproject

# Create data directory if it doesn't exist
mkdir -p data

# Create vendors.txt
cat > data/vendors.txt << 'EOF'
1,EcoFabrics Inc,contact@ecofabrics.com,false,false,Business License;Tax Certificate;Sustainability Certification
2,Premium Textiles Co,sales@premiumtextiles.com,true,false,Business License;ISO 9001 Certificate;Quality Assurance Docs
3,Blocked Vendor LLC,info@blocked.com,false,true,Business License
EOF

# Create contracts.txt
cat > data/contracts.txt << 'EOF'
1,1,Pending,Net 30 payment terms,100% organic materials,50000.00,,
2,2,Approved,Net 60 payment terms,50% recycled materials,75000.00,Budget verified and approved,All compliance requirements met. Approved for partnership.
EOF

# Create specifications.txt
cat > data/specifications.txt << 'EOF'
1,Smooth,150.0,100% recycled materials,Navy Blue,Matte
2,Textured,200.0,Organic cotton,Natural Beige,Glossy
3,Soft,180.0,Bamboo fiber blend,Forest Green,Satin
EOF

# Create prototypes.txt
cat > data/prototypes.txt << 'EOF'
1,Eco-Denim X1,Testing,70% recycled cotton\, 28% organic cotton\, 2% elastane,1,1,1;2
2,Sustainable Silk Pro,Approved,100% organic silk,2,2,3;4
EOF

# Create feedback.txt
cat > data/feedback.txt << 'EOF'
1,Good fabric strength. Minimal fraying during cutting. Suitable for industrial production.,4,Manufacturing,Mike Rodriguez
2,Color consistency excellent. Texture feels premium. Stone-wash needs adjustment.,3,Design,Alice Williams
3,Excellent drape and sheen. Perfect for evening wear collection.,5,Design,Alice Williams
4,Easy to work with. No special equipment needed.,5,Manufacturing,Mike Rodriguez
EOF

echo "✅ Sample data files created successfully!"
```

**Save as:** `setup_sample_data.sh`

**Make executable and run:**
```bash
chmod +x setup_sample_data.sh
./setup_sample_data.sh
```

---

## 🎯 What Each Setup Enables

### With Sample Data Loaded:

**Vendor Contract Demo Can:**
- Show existing vendors (including blocked one)
- Show contracts in different states (Pending, Approved)
- Demonstrate finance review on Contract ID 1
- Show persistence by viewing existing Contract ID 2

**Material Prototype Demo Can:**
- Use existing specifications (no need to create)
- Show prototype in Testing state
- Show approved prototype (final state)
- Add more feedback to existing prototype
- Refine existing prototype to Version 2

---

## 🧪 Testing the Sample Data

**Start application and verify:**

```bash
mvn clean compile exec:java -Dexec.mainClass="Main"

# Should see during startup:
# "Loaded 3 vendors from file."
# "Loaded 2 contracts from file."
# "Loaded 3 material specifications from file."
# "Loaded 4 feedback entries from file."
# "Loaded 2 prototypes from file."
```

**Navigate and verify:**
1. Main Menu → 8 → 3 (View All Contracts) - Should show 2 contracts
2. Main Menu → 9 → 3 (View All Prototypes) - Should show 2 prototypes

---

## 💡 Pro Tips

1. **For Clean Demo:** Start fresh and follow DEMO_SCRIPT.md
2. **For Quick Demo:** Use sample data to show mid-workflow states
3. **For Testing:** Use sample data and modify to test edge cases
4. **For Presentation:** Show empty start, then show with data after "restart"

---

## 🔄 Reset to Sample Data

**Anytime you want to reset:**
```bash
./setup_sample_data.sh
```

---

## 📝 CSV Format Notes

- **Commas in text:** Escaped with backslash `\,`
- **Semicolons:** Used for list separators (documents, feedback IDs)
- **Empty fields:** Just commas (no value between)
- **IDs:** First column always the ID
- **Relationships:** Foreign keys reference IDs

**Example:**
```
1,Eco-Denim X1,Testing,70% recycled cotton\, 28% organic cotton\, 2% elastane,1,1,1;2
^  ^            ^       ^                                                    ^  ^  ^
|  |            |       |                                                    |  |  |
ID Name        Status  Composition (escaped comma)                          Spec Feedback
                                                                            ID   IDs (1,2)
```

---

## ✅ Ready to Demo!

With this setup, you can:
- Demonstrate with clean data (Option 1)
- Or jump into mid-workflow scenarios (Option 2)
- Show data persistence by restarting
- Test all alternate flows
- Impress your professor! 🎓
