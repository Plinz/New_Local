/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NewLocalTestModule } from '../../../test.module';
import { PurchaseDetailComponent } from 'app/entities/purchase/purchase-detail.component';
import { Purchase } from 'app/shared/model/purchase.model';

describe('Component Tests', () => {
    describe('Purchase Management Detail Component', () => {
        let comp: PurchaseDetailComponent;
        let fixture: ComponentFixture<PurchaseDetailComponent>;
        const route = ({ data: of({ purchase: new Purchase(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchaseDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PurchaseDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PurchaseDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.purchase).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
