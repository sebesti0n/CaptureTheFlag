const knex = require("knex")(
  require("../Configuration/knexfile")["development"]
);

exports.userRegistration = async (req, res) => {
  const {
    Email,
    password,
    cnfpassword,
    FirstName,
    LastName,
    MobileNo,
    CollegeName,
    token,
    enroll_id,
  } = req;

  try {
    console.log(Email);
    const user = await knex("users")
      .insert({
        email: Email,
        password: password,
        cnfpassword: cnfpassword,
        FirstName: FirstName,
        LastName: LastName,
        MobileNo: MobileNo,
        CollegeName: CollegeName,
        enroll_id: enroll_id,
      })
      .returning("*");
    console.log(user);

    return res.status(200).json({ success: true, message: token, user: user[0] });
  } catch (error) {
    console.log(error);
    return res.status(400).json({ success: false, message: error, user: null });
  }
};
