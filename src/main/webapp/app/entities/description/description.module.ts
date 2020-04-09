import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RoyOpticalsSharedModule } from 'app/shared/shared.module';
import { DescriptionComponent } from './description.component';
import { DescriptionDetailComponent } from './description-detail.component';
import { DescriptionUpdateComponent } from './description-update.component';
import { DescriptionDeletePopupComponent, DescriptionDeleteDialogComponent } from './description-delete-dialog.component';
import { descriptionRoute, descriptionPopupRoute } from './description.route';

const ENTITY_STATES = [...descriptionRoute, ...descriptionPopupRoute];

@NgModule({
  imports: [RoyOpticalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DescriptionComponent,
    DescriptionDetailComponent,
    DescriptionUpdateComponent,
    DescriptionDeleteDialogComponent,
    DescriptionDeletePopupComponent
  ],
  entryComponents: [DescriptionDeleteDialogComponent]
})
export class RoyOpticalsDescriptionModule {}
