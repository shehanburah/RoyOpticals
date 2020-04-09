import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RoyOpticalsSharedModule } from 'app/shared/shared.module';
import { PrescriptionComponent } from './prescription.component';
import { PrescriptionDetailComponent } from './prescription-detail.component';
import { PrescriptionUpdateComponent } from './prescription-update.component';
import { PrescriptionDeletePopupComponent, PrescriptionDeleteDialogComponent } from './prescription-delete-dialog.component';
import { prescriptionRoute, prescriptionPopupRoute } from './prescription.route';

const ENTITY_STATES = [...prescriptionRoute, ...prescriptionPopupRoute];

@NgModule({
  imports: [RoyOpticalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PrescriptionComponent,
    PrescriptionDetailComponent,
    PrescriptionUpdateComponent,
    PrescriptionDeleteDialogComponent,
    PrescriptionDeletePopupComponent
  ],
  entryComponents: [PrescriptionDeleteDialogComponent]
})
export class RoyOpticalsPrescriptionModule {}
