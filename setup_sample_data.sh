#!/bin/bash
# Sample Data Setup Script for Fashion ERP System
# Creates pre-loaded data for demo purposes

cd /home/rahum/coms362finalproject

# Create data directory if it doesn't exist
mkdir -p data

echo "🔧 Setting up sample data files..."

# Create vendors.txt
cat > data/vendors.txt << 'EOF'
1,EcoFabrics Inc,contact@ecofabrics.com,false,false,Business License;Tax Certificate;Sustainability Certification
2,Premium Textiles Co,sales@premiumtextiles.com,true,false,Business License;ISO 9001 Certificate;Quality Assurance Docs
3,Blocked Vendor LLC,info@blocked.com,false,true,Business License
EOF
echo "✅ Created vendors.txt (3 vendors)"

# Create contracts.txt
cat > data/contracts.txt << 'EOF'
1,1,Pending,Net 30 payment terms,100% organic materials,50000.00,,
2,2,Approved,Net 60 payment terms,50% recycled materials,75000.00,Budget verified and approved,All compliance requirements met. Approved for partnership.
EOF
echo "✅ Created contracts.txt (2 contracts)"

# Create specifications.txt
cat > data/specifications.txt << 'EOF'
1,Smooth,150.0,100% recycled materials,Navy Blue,Matte
2,Textured,200.0,Organic cotton,Natural Beige,Glossy
3,Soft,180.0,Bamboo fiber blend,Forest Green,Satin
EOF
echo "✅ Created specifications.txt (3 specifications)"

# Create prototypes.txt
cat > data/prototypes.txt << 'EOF'
1,Eco-Denim X1,Testing,70% recycled cotton\, 28% organic cotton\, 2% elastane,1,1,1;2
2,Sustainable Silk Pro,Approved,100% organic silk,2,2,3;4
EOF
echo "✅ Created prototypes.txt (2 prototypes)"

# Create feedback.txt
cat > data/feedback.txt << 'EOF'
1,Good fabric strength. Minimal fraying during cutting. Suitable for industrial production.,4,Manufacturing,Mike Rodriguez
2,Color consistency excellent. Texture feels premium. Stone-wash needs adjustment.,3,Design,Alice Williams
3,Excellent drape and sheen. Perfect for evening wear collection.,5,Design,Alice Williams
4,Easy to work with. No special equipment needed.,5,Manufacturing,Mike Rodriguez
EOF
echo "✅ Created feedback.txt (4 feedback entries)"

echo ""
echo "🎉 Sample data setup complete!"
echo ""
echo "📊 Summary:"
echo "  - 3 Vendors (1 pending, 1 approved, 1 blocked)"
echo "  - 2 Contracts (1 pending, 1 approved)"
echo "  - 3 Material Specifications"
echo "  - 2 Prototypes (1 testing, 1 approved)"
echo "  - 4 Feedback entries"
echo ""
echo "🚀 Start the application:"
echo "   mvn clean compile exec:java -Dexec.mainClass=\"Main\""
echo ""
echo "📖 See DEMO_SCRIPT.md for detailed walkthrough"
