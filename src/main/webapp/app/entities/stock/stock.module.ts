import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NewLocalSharedModule } from 'app/shared';
import { NewLocalAdminModule } from 'app/admin/admin.module';
import {
    StockComponent,
    StockDetailComponent,
    StockUpdateComponent,
    StockDeletePopupComponent,
    StockDeleteDialogComponent,
    stockRoute,
    stockPopupRoute
} from './';

const ENTITY_STATES = [...stockRoute, ...stockPopupRoute];

@NgModule({
    imports: [NewLocalSharedModule, NewLocalAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [StockComponent, StockDetailComponent, StockUpdateComponent, StockDeleteDialogComponent, StockDeletePopupComponent],
    entryComponents: [StockComponent, StockUpdateComponent, StockDeleteDialogComponent, StockDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalStockModule {}
