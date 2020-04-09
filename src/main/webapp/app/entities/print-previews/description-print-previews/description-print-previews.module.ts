import { NgModule } from '@angular/core';
// import { CommonModule } from '@angular/common';
import { DescriptionPrintPreviewsComponent } from './description-print-previews.component';
import { RoyOpticalsSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { printRoute } from '../print-previews.route';



@NgModule({
  declarations: [DescriptionPrintPreviewsComponent],
  // imports: [
  //   DescriptionPrintPreviewsComponent
  // ],
  entryComponents:[
    DescriptionPrintPreviewsComponent
  ],
  imports: [
    RoyOpticalsSharedModule,
     RouterModule.forChild(printRoute)
  ]
})
export class DescriptionPrintPreviewsModule { }
