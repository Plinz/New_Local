import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { NewLocalPurchaseDoneModule } from './purchase-done/purchase-done.module';
import { NewLocalPurchasePendingModule } from './purchase-pending/purchase-pending.module';
import { NewLocalGradeModule } from './grade/grade.module';
import { NewLocalCategoryModule } from './category/category.module';
import { NewLocalProductTypeModule } from './product-type/product-type.module';
import { NewLocalLocationModule } from './location/location.module';
import { NewLocalStockModule } from './stock/stock.module';
import { NewLocalPersonModule } from './person/person.module';
import { NewLocalHoldingModule } from './holding/holding.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        NewLocalPurchaseDoneModule,
        NewLocalPurchasePendingModule,
        NewLocalGradeModule,
        NewLocalCategoryModule,
        NewLocalProductTypeModule,
        NewLocalLocationModule,
        NewLocalStockModule,
        NewLocalPersonModule,
        NewLocalHoldingModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NewLocalEntityModule {}
