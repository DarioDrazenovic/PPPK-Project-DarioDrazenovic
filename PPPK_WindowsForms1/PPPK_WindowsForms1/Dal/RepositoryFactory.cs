using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PPPK_WindowsForms1.Dal
{
    class RepositoryFactory
    {
        private static readonly Lazy<IRepository> repository = new Lazy<IRepository>(() => new Repository());
        public static IRepository GetRepository() => repository.Value;
    }
}
