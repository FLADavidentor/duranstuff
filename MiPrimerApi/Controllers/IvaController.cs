using Microsoft.AspNetCore.Mvc;

namespace MiPrimerApi.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class IvaController : ControllerBase
    {
        [HttpGet]
        public IActionResult Get([FromQuery] decimal precio)
        {
            var precioAfterIva = precio * 0.15m;

            return Ok(new
            {
                precio,
                precioafteriva = precioAfterIva
            });
        }
    }
}
