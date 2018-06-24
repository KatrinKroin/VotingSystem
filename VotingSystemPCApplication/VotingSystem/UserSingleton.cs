using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VotingSystem
{
    class UserSingleton
    {
        private static UserSingleton instance = new UserSingleton();
        private static User NewUser = null;
        private UserSingleton(){}
        private UserSingleton(String Email, String Password)
        {
            if (NewUser == null)
            {
                NewUser = new Server().Login(Email, Password);
            }
        }
        public static UserSingleton GetInstance()
        {
            if (instance == null ) throw new Exception("Object not created.");
            else if (NewUser == null) throw new Exception("The user doesn't exists in the system.");
            return instance;
        }

        public static void Create(String Email, String Password)
        {
            if (NewUser != null)
                throw new Exception("Object already created");
            instance = new UserSingleton(Email, Password);
        }


        public string GetUserID()
        {
            if (NewUser != null)
                return NewUser.UserID;
            else throw new Exception("The admin details is missing!");
        }
        public string GetName()
        {
            if (NewUser != null)
                return NewUser.Name;
            else throw new Exception("The admin details is missing!");
        }

        public string GetEmail()
        {
            if (NewUser != null)
                return NewUser.Email;
            else return "";
        }
        public string GetPassword()
        {
            if (NewUser != null)
                return NewUser.Password;
            else return "";
        }
        public bool GetAdmin()
        {
            if (NewUser != null)
                return NewUser.Admin;
            else throw new Exception("The admin details is missing!");
        }
    }
}
