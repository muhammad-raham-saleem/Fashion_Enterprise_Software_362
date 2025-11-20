# 🎬 DEMO SCRIPT - Fashion ERP System
## Use Cases: Vendor Contract Approval & Material Prototype Development

---

## 🚀 SETUP

**Start the Application:**
```bash
cd /home/rahum/coms362finalproject
mvn clean compile exec:java -Dexec.mainClass="Main"
```

---

## 📋 DEMO 1: VENDOR CONTRACT APPROVAL WORKFLOW

### **Scenario:**
*EcoFabrics Inc wants to become a supplier. They've submitted all required documents and are offering sustainable materials at $50,000 budget.*

---

### **STEP 1: Create a New Vendor**

**Navigation:** Main Menu → `8` (Vendor Contract Management) → `1` (Create New Vendor)

**Input:**
```
Enter vendor name: EcoFabrics Inc
Enter contact info: contact@ecofabrics.com
How many documents to add? 3

Enter document 1 name: Business License
Enter document 2 name: Tax Certificate
Enter document 3 name: Sustainability Certification
```

**Expected Output:**
```
Vendor created successfully! Vendor ID: 1
```

**✅ Key Point:** Vendor is now saved to `data/vendors.txt`

---

### **STEP 2: Create a Contract**

**Navigation:** From Vendor Menu → `2` (Create New Contract)

**Input:**
```
Enter vendor ID: 1
Enter pricing terms: Net 30 payment terms, 5% bulk discount
Enter sustainability requirements: 100% organic cotton, zero-waste packaging
Enter budget amount: $50000
```

**Expected Output:**
```
Contract created successfully! Contract ID: 1
```

**✅ Key Point:** Contract starts with status "Pending" and saved to `data/contracts.txt`

---

### **STEP 3: View Pending Contracts**

**Navigation:** From Vendor Menu → `4` (View Pending Contracts)

**Expected Output:**
```
=== PENDING CONTRACTS ===
Contract ID: 1, Vendor: EcoFabrics Inc, Status: Pending, Budget: $50000.00
```

---

### **STEP 4: Finance Manager Review**

**Navigation:** From Vendor Menu → `5` (Finance Review)

**What Happens:**
- System shows all pending contracts
- Finance Manager (John Smith) logs in automatically

**Input:**
```
Enter contract ID to review: 1

[System displays full contract details]

Enter available budget: $75000
Enter finance notes: Budget approved. Sustainable sourcing aligns with company goals.
Submit for legal review? (yes/no): yes
```

**Expected Output:**
```
Contract submitted for legal review.
```

**✅ Key Point:** Contract status updated to "Legal Review" in file

---

### **STEP 5: Legal Officer Review**

**Navigation:** From Vendor Menu → `6` (Legal Review)

**What Happens:**
- System shows contracts in legal review
- Legal Officer (Sarah Johnson) logs in automatically
- System runs compliance check

**Input:**
```
Enter contract ID to review: 1

[System displays contract details]

Compliance Check: PASSED

Enter legal notes: All certifications verified. Terms comply with trade regulations.
Approve contract? (yes/no): yes
```

**Expected Output:**
```
Contract approved! Vendor is now an active supplier.
```

**✅ Key Point:**
- Contract status → "Approved"
- Vendor approved → true
- Both saved to files

---

### **STEP 6: Verify Final Status**

**Navigation:** From Vendor Menu → `7` (View Contract Details)

**Input:**
```
Enter contract ID: 1
```

**Expected Output:**
```
=== CONTRACT DETAILS ===
Contract ID: 1
Vendor: EcoFabrics Inc
Status: Approved
Pricing Terms: Net 30 payment terms, 5% bulk discount
Sustainability Requirements: 100% organic cotton, zero-waste packaging
Budget Amount: $50000.00
Finance Notes: Budget approved. Sustainable sourcing aligns with company goals.
Legal Notes: All certifications verified. Terms comply with trade regulations.
```

---

### **STEP 7: Persistence Test**

**Exit and Restart:**
```
[Select 0 to return to main menu]
[Select 0 to exit]
[Restart application]
```

**Navigate:** Main Menu → `8` → `3` (View All Contracts)

**✅ Expected:** Contract still shows "Approved" - Data persisted! 🎉

---

## 🔬 DEMO 2: MATERIAL PROTOTYPE DEVELOPMENT WORKFLOW

### **Scenario:**
*Design team requested eco-friendly denim. R&D needs to develop a prototype using recycled materials, get feedback from manufacturing and design teams, then refine based on input.*

---

### **STEP 1: Create Material Specification**

**Navigation:** Main Menu → `9` (Material Prototype Development) → `1` (Create Material Specification)

**Input:**
```
Enter texture: Smooth with slight texture
Enter weight (grams): 350
Enter sustainability requirement: 70% recycled cotton, 30% organic cotton
Enter color: Indigo Blue
Enter finish: Stone-washed
```

**Expected Output:**
```
Specification created successfully! Spec ID: 1
```

**✅ Key Point:** Specification saved to `data/specifications.txt`

---

### **STEP 2: R&D Scientist Creates Prototype**

**Navigation:** From Prototype Menu → `2` (Create New Prototype - R&D Scientist)

**What Happens:**
- System shows available specifications
- Dr. Emily Chen (R&D Scientist) logs in

**Input:**
```
Enter specification ID: 1
Enter prototype name: Eco-Denim X1
Enter composition: 70% recycled cotton, 28% organic cotton, 2% elastane
```

**Expected Output:**
```
Prototype created successfully! Prototype ID: 1
Status updated to: Testing
```

**✅ Key Point:** Prototype auto-advances to "Testing" status

---

### **STEP 3: Manufacturing Technician Tests**

**Navigation:** From Prototype Menu → `5` (Manufacturing Test)

**What Happens:**
- System shows prototypes in "Testing" status
- Mike Rodriguez (Manufacturing Technician) logs in

**Input:**
```
Enter prototype ID to test: 1

[System displays prototype details]

=== MANUFACTURABILITY TEST ===
Enter test comments: Good fabric strength. Minimal fraying during cutting. Suitable for industrial production with standard equipment.
Enter rating (1-5): 4
```

**Expected Output:**
```
Manufacturing feedback added successfully!
Rating: 4/5
```

**✅ Key Point:** Feedback saved to `data/feedback.txt` and linked to prototype

---

### **STEP 4: Design Team Evaluates**

**Navigation:** From Prototype Menu → `6` (Design Evaluation)

**What Happens:**
- Alice Williams (Senior Designer) logs in
- Evaluates aesthetic quality

**Input:**
```
Enter prototype ID to evaluate: 1

[System displays prototype details]

=== AESTHETIC EVALUATION ===
Enter aesthetic comments: Color consistency excellent. Texture feels premium. Stone-wash finish needs slight adjustment for better vintage appearance.
Enter rating (1-5): 3
```

**Expected Output:**
```
Design feedback added successfully!
Rating: 3/5
Low rating detected. Color/finish may need adjustment.
```

**✅ Key Point:** System flags low rating for attention

---

### **STEP 5: View All Feedback**

**Navigation:** From Prototype Menu → `4` (View Prototype Details)

**Input:**
```
Enter prototype ID: 1
```

**Expected Output:**
```
=== PROTOTYPE DETAILS ===
Prototype ID: 1
Name: Eco-Denim X1
Status: Testing
Version: 1
Composition: 70% recycled cotton, 28% organic cotton, 2% elastane
Specification: Texture: Smooth with slight texture, Weight: 350.0g, Color: Indigo Blue, Finish: Stone-washed, Sustainability: 70% recycled cotton, 30% organic cotton
Feedback Count: 2

=== FEEDBACK ===
Feedback ID: 1, Type: Manufacturing, Rating: 4/5, Reviewer: Mike Rodriguez, Comments: Good fabric strength. Minimal fraying during cutting. Suitable for industrial production with standard equipment.

Feedback ID: 2, Type: Design, Rating: 3/5, Reviewer: Alice Williams, Comments: Color consistency excellent. Texture feels premium. Stone-wash finish needs slight adjustment for better vintage appearance.
```

---

### **STEP 6: R&D Refines Based on Feedback**

**Navigation:** From Prototype Menu → `7` (Refine Prototype - R&D Scientist)

**What Happens:**
- Dr. Emily Chen reviews all feedback
- Adjusts composition based on design feedback

**Input:**
```
Enter prototype ID to refine: 1

[System displays prototype and all feedback]

Enter new composition: 70% recycled cotton, 28% organic cotton, 2% elastane with enhanced enzyme wash process
```

**Expected Output:**
```
Prototype refined successfully!
New version: 2
New composition: 70% recycled cotton, 28% organic cotton, 2% elastane with enhanced enzyme wash process
```

**✅ Key Point:** Version increments automatically, composition updated

---

### **STEP 7: Re-test After Refinement**

**Repeat Steps 3-4 with higher ratings:**

**Manufacturing Test (Round 2):**
```
Rating: 5
Comments: Excellent results. Ready for mass production.
```

**Design Evaluation (Round 2):**
```
Rating: 5
Comments: Perfect vintage appearance achieved. Premium look and feel. Approved for collection.
```

---

### **STEP 8: Final Approval**

**Navigation:** From Prototype Menu → `8` (Approve/Reject Prototype)

**Input:**
```
Enter prototype ID to approve/reject: 1

[System displays details and calculates average rating]

Average Rating: 4.2/5

1. Approve for Production Testing
2. Reject Prototype
Choose option: 1
```

**Expected Output:**
```
Prototype approved for production testing!
```

**✅ Key Point:** Status → "Approved", ready for next phase

---

### **STEP 9: View Final Prototype Status**

**Navigation:** From Prototype Menu → `3` (View All Prototypes)

**Expected Output:**
```
=== ALL PROTOTYPES ===
Prototype ID: 1, Name: Eco-Denim X1, Status: Approved, Version: 2
```

---

### **STEP 10: Persistence Test**

**Exit and Restart:**
```
[Exit application]
[Restart]
Main Menu → 9 → 3 (View All Prototypes)
```

**✅ Expected:**
- Prototype shows Version 2
- Status "Approved"
- All feedback preserved
- Data persists across sessions! 🎉

---

## 🎯 ALTERNATE FLOWS TO DEMONSTRATE

### **Vendor Contract - Rejection Flow**

**At Finance Review Step:**
```
Enter available budget: $30000
[Budget insufficient - $50,000 requested vs $30,000 available]
Reject contract? (yes/no): yes
```

**Result:** Contract status → "Rejected", vendor notified

---

### **Vendor Contract - Compliance Failure**

**Create vendor with no documents:**
```
How many documents to add? 0
```

**At Legal Review:**
```
Compliance Check: FAILED
Vendor does not meet compliance requirements.
Block vendor? (yes/no): yes
```

**Result:** Vendor blocked, cannot create new contracts

---

### **Material Prototype - Rejection Flow**

**At Approval Step:**
```
Average Rating: 2.1/5
Choose option: 2
```

**Result:** Prototype rejected, back to R&D for major redesign

---

## 📊 DATA FILES INSPECTION

**After running demos, inspect the data files:**

```bash
# View vendors
cat data/vendors.txt

# View contracts
cat data/contracts.txt

# View specifications
cat data/specifications.txt

# View prototypes
cat data/prototypes.txt

# View feedback
cat data/feedback.txt
```

**You'll see CSV-formatted data with all details persisted!**

---

## 🎓 TALKING POINTS FOR PRESENTATION

1. **Separation of Concerns:**
   - Models handle data structure
   - Services handle business logic
   - Repositories handle persistence
   - Menus handle user interaction

2. **Multi-Stage Approval Workflows:**
   - Both use cases implement state machines
   - Clear status transitions
   - Role-based access control

3. **Data Integrity:**
   - Foreign key relationships (Vendor → Contract, Spec → Prototype)
   - Referential integrity maintained
   - Automatic ID generation

4. **Professional Patterns:**
   - Repository pattern (like existing CampaignRepository)
   - Service layer pattern
   - CSV serialization with escaping
   - Version control for prototypes

5. **User Experience:**
   - Clear menu navigation
   - Validation and error messages
   - Contextual prompts
   - Status tracking

---

## 🏆 SUCCESS CRITERIA CHECKLIST

**Use Case 1 - Vendor Contract Approval:**
- ✅ Vendor creation with documents
- ✅ Contract draft creation
- ✅ Finance Manager review with budget check
- ✅ Legal Officer compliance check
- ✅ Multi-stage approval workflow
- ✅ Vendor blocking mechanism
- ✅ Status updates persist to files
- ✅ Active supplier activation

**Use Case 2 - Material Prototype Development:**
- ✅ Material specification creation
- ✅ R&D prototype creation
- ✅ Manufacturing technician testing
- ✅ Design team evaluation
- ✅ Multi-stakeholder feedback system
- ✅ Rating system (1-5 scale)
- ✅ Prototype refinement with versioning
- ✅ Final approval workflow
- ✅ All data persists across sessions

---

## 🔧 TROUBLESHOOTING

**If data files don't exist:**
- They'll be created automatically on first save
- Start with creating vendors/specifications

**If compilation fails:**
```bash
mvn clean compile
```

**To reset all data:**
```bash
rm data/vendors.txt data/contracts.txt data/specifications.txt data/prototypes.txt data/feedback.txt
```

---

## 🎬 END OF DEMO SCRIPT

**Total Demo Time:** ~15-20 minutes for both use cases

**This demo showcases:**
- Complete workflows matching use case requirements
- Professional file persistence
- Multi-role collaboration
- Data integrity and validation
- Real-world business scenarios

**Ready for your class presentation! 🚀**
