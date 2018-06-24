using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;
using System.Windows.Threading;

namespace VotingSystem
{
    class Voting
    {
        private BitmapImage votePicture;
        public BitmapImage VotePicture {
            get {
                BitmapImage btm;
                if (AvailableVoting() == true) btm = new BitmapImage(new Uri("/image/sceopen.png", UriKind.Relative));
                else if (FutureVoting() == true) btm = new BitmapImage(new Uri("/image/scefuture.png", UriKind.Relative));
                else btm = new BitmapImage(new Uri("/image/sceclosed.png", UriKind.Relative));
                votePicture = btm;
                return votePicture; }
            set { votePicture = value; }
        }
        public string VoteNum { get; set; }
        public DateTime Start { get; set; }
        public DateTime Finish { get; set; }
        public string VoteName { get; set; }
        public string VoteDescription { get; set; }
        public string Timer { get; set; }

        public bool AvailableVoting() {
            DateTime Current = DateTime.Now;
            int result1 = DateTime.Compare(Start, Current);
            int result2 = DateTime.Compare(Finish, Current);
            if (result1 < 0 && result2 > 0) return true;
            else return false;
        }

        public bool FutureVoting()
        {
            DateTime Current = DateTime.Now;
            int result1 = DateTime.Compare(Start, Current);
            if (result1 > 0) return true;
            else return false;
        }
    }
}
