import { Component, OnInit } from '@angular/core';
import { IDescription } from 'app/shared/model/description.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-print-previews',
  templateUrl: './description-print-previews.component.html',
  styleUrls: ['./description-print-previews.component.scss']
})
export class DescriptionPrintPreviewsComponent implements OnInit {
  description: IDescription;
  negombo=false;
  dankotuwa=false;
  wennapuwa=false;
  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ description }) => {
      this.description = description;
    });
  }
}
