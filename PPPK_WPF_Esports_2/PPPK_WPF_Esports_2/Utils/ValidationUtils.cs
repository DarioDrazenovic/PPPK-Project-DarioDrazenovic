using System.Text.RegularExpressions;

namespace PPPK_WPF_Esports_2.Utils
{
    public static class ValidationUtils
	{
		// constants!
		public static bool IsValidEmail(string email) => Regex.IsMatch
			(email, 
			@"\A(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?)\Z", RegexOptions.IgnoreCase);
	}
}
