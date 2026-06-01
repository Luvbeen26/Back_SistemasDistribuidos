const errorHandler = (err,req,res,next) =>{
    console.error(err.message || err);
    res.status(500).json({ message : 'Error del servidor', error: err.message });
}

module.exports = errorHandler;