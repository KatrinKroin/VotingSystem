using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Mail;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media.Imaging;

namespace VotingSystem
{
    class User
    {
        private BitmapImage userPicture;
        public BitmapImage UserPicture
        {
            get
            {
                BitmapImage btm = new BitmapImage(new Uri("/image/candidates.png", UriKind.Relative));
                userPicture = btm;
                return userPicture;
            }
            set { userPicture = value; }
        }
        public string UserID { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public bool Admin { get; set; }
    }
}
