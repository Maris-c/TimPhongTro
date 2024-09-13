const cloudinary = require('cloudinary').v2;

cloudinary.config({ 
    cloud_name: "dzflik4ks", 
    api_key: "696667416842623", 
    api_secret: "Homsf7lea0Z6Hjx99HWlq1W2Y1w"
});

module.exports = cloudinary;