const knex = require('knex')(require('../Configuration/knexfile')['development']);



exports.userRegistration = async(req,res)=>{
    const {newUser , token } = req;
    const n_user = {
        name:newUser.FirstName,
        email:newUser.Email
    };

    newUser.save().then(() => {
        // console.log("all is well");
        knex('users')
        .insert(n_user)
        .returning('*')
        .then((inserted_user)=>{
            console.log("inserted Data",inserted_user);
        })
        .catch((err)=>{
            console.log(err);
        })
        .finally(()=>{
            knex.destroy();
        });
        return res.status(200).json({success:true, message:token, user: newUser});
    }).catch((e) => {
        // console.log("dikkat");
        // console.log(e);
        return res.status(400).json({success:false, message:e, user: null});
    });
}