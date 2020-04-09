import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDescription } from 'app/shared/model/description.model';

@Component({
  selector: 'jhi-description-detail',
  templateUrl: './description-detail.component.html'
})
export class DescriptionDetailComponent implements OnInit {
  description: IDescription;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ description }) => {
      this.description = description;
    });
  }

  previousState() {
    window.history.back();
  }
}
