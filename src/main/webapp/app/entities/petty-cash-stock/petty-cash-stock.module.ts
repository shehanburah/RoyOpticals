import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RoyOpticalsSharedModule } from 'app/shared/shared.module';
import { PettyCashStockComponent } from './petty-cash-stock.component';
import { PettyCashStockDetailComponent } from './petty-cash-stock-detail.component';
import { PettyCashStockUpdateComponent } from './petty-cash-stock-update.component';
import { PettyCashStockDeletePopupComponent, PettyCashStockDeleteDialogComponent } from './petty-cash-stock-delete-dialog.component';
import { pettyCashStockRoute, pettyCashStockPopupRoute } from './petty-cash-stock.route';

const ENTITY_STATES = [...pettyCashStockRoute, ...pettyCashStockPopupRoute];

@NgModule({
  imports: [RoyOpticalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PettyCashStockComponent,
    PettyCashStockDetailComponent,
    PettyCashStockUpdateComponent,
    PettyCashStockDeleteDialogComponent,
    PettyCashStockDeletePopupComponent
  ],
  entryComponents: [PettyCashStockDeleteDialogComponent]
})
export class RoyOpticalsPettyCashStockModule {}
