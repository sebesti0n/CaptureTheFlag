const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const knex = require("knex")(
  require("../Configuration/knexfile")["development"]
);

const registrationMiddleware = async (req, res, next) => {
  try {
    let {
      Email,
      password,
      cnfpassword,
      FirstName,
      LastName,
      MobileNo,
      CollegeName,
      enroll_id,
    } = req.body;
    // console.log(newUser);
    const alreadyPresent = await knex("users")
      .select("*")
      .where("email", "=", Email);

    if (alreadyPresent.length != 0) {
      return res
        .status(200)
        .json({
          success: false,
          message: "user already registered",
          user: null,
        });
    } else {
      bcrypt.hash(password, 10, (err, hashedpassword) => {
        if (err) return next(err);
        password = hashedpassword;
        cnfpassword = password;
        let data = {
          time: Date(),
          userEmail: Email,
        };
        const token = jwt.sign(data, process.env.JWT_SECRET_KEY);
        req.token = token;
        req.Email = Email;
        req.password = password;
        req.cnfpassword = cnfpassword;
        req.FirstName = FirstName;
        req.LastName = LastName;
        req.MobileNo = MobileNo;
        req.CollegeName = CollegeName;
        req.enroll_id = enroll_id;
        next();
      });
    }
  } catch (error) {
    console.log("Error in registration middleware:", error);
    return res
      .status(500)
      .json({ success: false, message: "Internal Server Error" });
  }
};

module.exports = registrationMiddleware;
