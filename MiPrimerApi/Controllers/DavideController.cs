using Microsoft.AspNetCore.Mvc;

namespace MiPrimerApi.Controllers;

[ApiController]
[Route("[controller]")] // Route: davide
public class DavideController : ControllerBase
{
    // GET: davide
    [HttpGet]
    public IActionResult GetInfo()
    {
        DavideInfo info = new DavideInfo
        {
            Name = "Davide",
            Age = 30,
            Email = "davide@example.com"
        };
        return Ok(info);
    }
}

public class DavideInfo
{
    public string Name { get; set; }
    public int Age { get; set; }
    public string Email { get; set; }
}