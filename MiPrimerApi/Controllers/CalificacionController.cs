using Microsoft.AspNetCore.Mvc;

namespace MiPrimerApi.Controllers;

[ApiController]
[Route("[controller]")] // Route: /calificacion
public class CalificacionController : ControllerBase
{
    [HttpGet]
    public IActionResult Clasificar([FromQuery] int puntos)
    {
        if (puntos < 0 || puntos > 100)
        {
            return BadRequest(new { error = "Los puntos deben estar entre 0 y 100." });
        }

        string nivel = puntos switch
        {
            >= 90 => ("Aprendizaje Avanzado"),
            >= 80 => ("Aprendizaje Satisfactorio"),
            >= 70 => ("Aprendizaje Fundamental"),
            _     => ("Aprendizaje Inicial")
        };

        return Ok(new
        {
            puntos,
            nivel
        });
    }
}