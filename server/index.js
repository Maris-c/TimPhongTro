// Khai bao port: 
const express = require('express')
const app = express()
const port = 3000
const roomsRoute = require('./routes/rooms')
const usersRoute = require('./routes/users')
const addressesRoute = require('./routes/addresses')
const imagesRoute = require('./routes/images');
const favoritesRoute = require('./routes/favorites');

app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ limit: '50mb', extended: true }));

app.use(
    express.urlencoded({
        extended: true
    })
)

//route
app.get('/', (req, res) =>{
    res.json({message: 'Tạo API với Node JS + mySQL'})
})

// Room
app.use('/', roomsRoute)

// User
app.use('/', usersRoute)

// Address
app.use('/', addressesRoute)

// Favorite
app.use('/', favoritesRoute)

//Upload image
app.use('/', imagesRoute)

// Error handler
app.use((err, req, res, next) => {
    const statusCode = err.statusCode || 500;
    console.error(err.message, err.stack);
    res.status(statusCode).json({message: err.message});
    return;
});

app.listen(port, () =>{
    console.log(`Server is running on https://localhost:${port}`)
})