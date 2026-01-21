import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-user-activate',
  templateUrl: './user-activate.component.html',
  styleUrls: ['./user-activate.component.css']
})
export class UserActivateComponent implements OnInit{
  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
  ) {}

  ngOnInit(): void{
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if(token){
        this.authService.activate(token).subscribe({
          next: () => alert("Yay!"),
          error: (err) => {
            console.error(err);
            alert('Validation failed: ' + err.error);
          }
        });
      }
      else {
        alert("invalid verification link");
      }
    });
  }
}
