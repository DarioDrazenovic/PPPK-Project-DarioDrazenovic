using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PPPK_WPF_Esports_2.Dal
{
    public static class RepositoryFactory
    {
        private static readonly Lazy<IRepository> repository = new Lazy<IRepository>(() => new SqlRepository());

        public static IRepository GetRepository() => repository.Value;

        private static readonly Lazy<IRepositoryEsports> repositoryTeam = new Lazy<IRepositoryEsports>(() => new SqlRepositoryEsports());
        public static IRepositoryEsports GetRepositoryEsports() => repositoryTeam.Value;
    }
}
